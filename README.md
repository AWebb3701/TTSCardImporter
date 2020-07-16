# TTSCardImporter
Java program to generate a Saved Object JSON from the .png files in a given directory, filtering out non-png files and leaving a playable TableTop Simulator deck file.

Usage notes:
As I haven't figured out how to properly iterate through the files in a remote directory, the program currently requires both an external host for the actual .png files AND a local directory with files that have the same naming pattern (CARDNAME_O1, CARDNAME_02, etc).

Intended to be used alongside http://www.nand.it/nandeck/, which generates files with that naming convention in a local directory.

To use:
Run the .jar in a terminal, follow the prompts. On first run it'll use your inputs to generate its own .ini file, which it will use for all further runs unless you delete it. When it's done it'll create a JSON in the same directory it was run from.


Cleaning Tasks:
Split functions into new classes
Clean up loops (I use the same if loop in 3 spots...)
Choose a variable declaration standard, I declared them all over the place with no rhyme or reason.

Feature TODO:
Remove the need for a local mirror by iterating over the remote directory instead.

Changelog:
7/15/20 - Fixed a typo that caused the json file to have a capitalized extension, which TTS won't recognize.
