package com.mycompany.ttscardimporter;


import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 *
 * @author Alexander Webb
 */

public class Main {
    
    public static void main(String[] args) throws IOException {

        Scanner userinput = new Scanner(System.in);
        String BackURL = "";
        String remoteDirectory = "";
        String pathtodirectory = "";
            String JSONTemplate = "{\n" +
"  \"SaveName\": \"\",\n" +
"  \"GameMode\": \"\",\n" +
"  \"Gravity\": 0.5,\n" +
"  \"PlayArea\": 0.5,\n" +
"  \"Date\": \"\",\n" +
"  \"Table\": \"\",\n" +
"  \"Sky\": \"\",\n" +
"  \"Note\": \"\",\n" +
"  \"Rules\": \"\",\n" +
"  \"XmlUI\": \"\",\n" +
"  \"LuaScript\": \"\",\n" +
"  \"LuaScriptState\": \"\",\n" +
"  \"ObjectStates\": [\n" +
"    {\n" +
"      \"Name\": \"Deck\",\n" +
"      \"Transform\": {\n" +
"        \"posX\": -29.9,\n" +
"        \"posY\": 1.04493034,\n" +
"        \"posZ\": 17.6,\n" +
"        \"rotX\": -3.54128224E-06,\n" +
"        \"rotY\": -1.20639479E-05,\n" +
"        \"rotZ\": 180.0,\n" +
"        \"scaleX\": 1.0,\n" +
"        \"scaleY\": 1.0,\n" +
"        \"scaleZ\": 1.0\n" +
"      },\n" +
"      \"Nickname\": \"\",\n" +
"      \"Description\": \"\",\n" +
"      \"GMNotes\": \"\",\n" +
"      \"ColorDiffuse\": {\n" +
"        \"r\": 0.713235259,\n" +
"        \"g\": 0.713235259,\n" +
"        \"b\": 0.713235259\n" +
"      },\n" +
"      \"Locked\": false,\n" +
"      \"Grid\": true,\n" +
"      \"Snap\": true,\n" +
"      \"IgnoreFoW\": false,\n" +
"      \"Autoraise\": true,\n" +
"      \"Sticky\": true,\n" +
"      \"Tooltip\": true,\n" +
"      \"GridProjection\": false,\n" +
"      \"HideWhenFaceDown\": true,\n" +
"      \"Hands\": false,\n" +
"      \"SidewaysCard\": false,\n" +
"      \"DeckIDs\": [\n" +
"      ],\n" +
"      \"CustomDeck\": {\n" +
"      },\n" +
"      \"XmlUI\": \"\",\n" +
"      \"LuaScript\": \"\",\n" +
"      \"LuaScriptState\": \"\",\n" +
"      \"ContainedObjects\": [\n" +
"    ],\n" +
"      \"GUID\": \"02bfa1\"\n" +
"    }\n" +
"  ],\n" +
"  \"TabStates\": {},\n" +
"  \"VersionNumber\": \"\"\n" +
"}";
        File inifile = new File("CardImporter.ini");
        boolean ini = inifile.exists();
        Properties prop = new Properties();
        //Check if there's a CardImporter.ini file in the same directory. If there isn't, prompt for input. an ini will be created later. 
        if (ini){
        FileInputStream inifis = new FileInputStream("CardImporter.ini");
        prop.load(inifis);
        }
        //Generate the JSON template
        File json = new File("Cards.JSON");
        if (json.createNewFile()){
        System.out.println("JSON Template Generated");            
        }
        try (PrintWriter out = new PrintWriter("Cards.JSON")){
            out.println(JSONTemplate);
        }
           if (!ini){
            System.out.println("Input the url of your card back. \nExample: http://website.com/cardimages/cardback.png \nThere's no error checking here, so be precise.");
            BackURL = userinput.nextLine();
            }
            else{
            BackURL = prop.getProperty("CardBackURL");
            }
            
            if (!ini){
            System.out.println("Input the remote directory that you are hosting your images at: \nExample: http://website.com/cardimages/ \nThere's no error checking here, so be precise.");
            remoteDirectory = userinput.nextLine();
            }
            else{
            remoteDirectory = prop.getProperty("RemoteDirectory");
            }
            if (!ini){
            System.out.println("Input directory to be converted:");
            pathtodirectory = userinput.nextLine();
            }
            else{
            pathtodirectory = prop.getProperty("LocalDirectory");
            }

        File dir = new File(pathtodirectory);
            while (!dir.isDirectory()){
                System.out.println("That's not a valid directory, try again!");
                pathtodirectory = userinput.nextLine();
                dir = new File(pathtodirectory);
            }
        //Count and scan the list of files for "png" format files.
        int filecount = dir.list().length;
        System.out.println("Checking " + filecount + " files and skipping non-pngs...");
        File[] directoryListing = dir.listFiles();            
        for (File child : directoryListing) {
            String filename = child.getName(); 
            if("png".equals(getFileExtension(child))){
            System.out.println(filename + " is a valid png.");
            
            //Get the card number by trimming all non-numbers from the filename.
            String cardnumber = filename.replaceAll("[^0-9]", "");
            int cardnumberInt = Integer.parseInt(cardnumber);
            int firstDigit = Integer.parseInt(cardnumber.substring(0,1));
            String cardnumber00 = "";
            //Strip any leading zeroes
            if (firstDigit == 0){
                System.out.println(cardnumber + " has a leading 0, trimming it.");
                cardnumber = cardnumber.replaceAll("^0(?!$)", "");
                }
            else{
                System.out.println(filename + "'s Card number is " + cardnumber + ".");                
            }
            //add some zeroes for deckID purposes.
            cardnumber00 = (cardnumber + "00");
            String FaceURL = (remoteDirectory+filename);
            String search = "\"CustomDeck\": \\{";
            String replace = "\"CustomDeck\": \\{\n"                
                        +"        \""+cardnumber+"\": \\{\n"
                        +"          \"FaceURL\": \""+FaceURL+"\",\n"
                        +"          \"BackURL\": \""+BackURL+"\",\n"
                        +"          \"NumWidth\": 1,\n"
                        +"          \"NumHeight\": 1,\n"
                        +"          \"BackIsHidden\": true,\n"
                        +"          \"UniqueBack\": false\n"
                        +"        },\n";
            String search2 = "\"DeckIDs\": \\[";
            String replace2 = "\"DeckIDs\": \\[\n"                
                        +"        "+cardnumber00+",";
            
            //regex shenanigans block
            Scanner sc = new Scanner(new File("Cards.JSON"));
            StringBuffer buffer = new StringBuffer();
                while (sc.hasNextLine()){
                    buffer.append(sc.nextLine()+System.lineSeparator());
                }
                String fileContents = buffer.toString();
                sc.close();
                fileContents = fileContents.replaceFirst(search, replace);
                fileContents = fileContents.replaceFirst(search2, replace2);
                FileWriter writer = new FileWriter("Cards.JSON");
                writer.append(fileContents);
                writer.flush();  
            }
            //end regex shenanigans block
            else{
                System.out.println(filename + " is NOT a valid png, skipping... ") ;   
            }
        }
        System.out.println("Done!");
        if (ini){
            System.out.println("Edit or delete CardImporter.ini to change your target files/directories.\n You may now safely close this window.");
            }
        else{
        //create a new file named CardImporter.ini
        String inicontents =
        "LocalDirectory = "+pathtodirectory+"\n"
       +"RemoteDirectory = "+remoteDirectory+"\n"
       +"CardBackURL = "+BackURL;

        try (PrintWriter out = new PrintWriter("CardImporter.ini")){
            out.println(inicontents);
        }
        System.out.println("CardImporter.ini has been created using the choices made this session. \n Edit or delete it if you want to change your target files/directories.\n You may now safely close this window.");    
        }
        
    }
    
//Shamelessly copied from StackExchange
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
//Shamelessly copied from StackExchange
    public static void copyWithDifferentName(File sourceFile, String newFileName) {
    if (sourceFile == null || newFileName == null || newFileName.isEmpty()) {
        return;
    }
    String extension = "";
    if (sourceFile.getName().split("\\.").length > 1) {
        extension = sourceFile.getName().split("\\.")[sourceFile.getName().split("\\.").length - 1];
    }
    String path = sourceFile.getAbsolutePath();
    String newPath = path.substring(0, path.length() - sourceFile.getName().length()) + newFileName;
    if (!extension.isEmpty()) {
        newPath += "." + extension;
    }
    try (OutputStream out = new FileOutputStream(newPath)) {
        Files.copy(sourceFile.toPath(), out);
    } catch (IOException e) {
        e.printStackTrace();
      }
    }

}

