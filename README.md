# MyShogi
Minishogi, a Japanese variant of chess. it works very similar to Western chess but is played on a smaller board with some different pieces and rules. 

The project was my box take-home interview project. The public test file and Utils.class files were given by the interviewer. I used the functions in Utils.class to do some of the tedious input and output. 

## Running the tests
test-runner-mac runs all the test files in the public test foler
```
./test-runner-mac 
```
If you want to run a specific test file, run the myShogi.java file with the file path. Compare the output with the output test file
```
javac Bishop.java GoldGeneral.java myShogi.java King.java Rook.java Pawn.java ShogiGame.java Piece.java SilverGeneral.java Player.java Utils.java 
```
```
java myShogi.java -f Public\ Test\ Cases/basicCheck.in
```
If you want to play the game and place the piece by yourself. You can choose -i mode. 
```
javac Bishop.java GoldGeneral.java myShogi.java King.java Rook.java Pawn.java ShogiGame.java Piece.java Silv
```
```
java myShogi -i
```
