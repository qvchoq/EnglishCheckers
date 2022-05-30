# English Checkers
___
## Rules
* Red checkers make the first turn. 
* "Default" checkers can move diagonally one cell forward and beat only forward. 
* The queen can move one cell diagonally forward or backward, when beaten, it moves only through one cell in any direction.
* Beating the opponent's checker is necessarily. 
* With several options for beating, the player chooses the option of beating at his discretion, and in the selected
  option it is necessary to beat all the checkers available for beating.
* After 30 turns without beating - draw.
___
## Documentation
### Description

English checkers for 2 players on one phone.

### Values description
> [GameActivity.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/controller/GameActivity.kt)
* **boardSize** - board size.
* **fromX** - coordinate X where from picked up the checker.
* **fromY** - coordinate Y where from picked up the checker.
* **prevCellName** - previous cell name coordinates from picked up the checker.
* **turnsWithoutBeating** - turns without any beating.
* **hasBeat** - if checker was beaten.
* **hasMoved** - if checker was moved.
* **needToBeatNow** - if you need to beat now.
* **replacedToQueen** - if default checker was replaced to queen.
* **possibleMoves** - map (cell name -> list of moves), with possible moves for checker.
* **mustToMoveList** - list with cell name coordinates, where you must move.
> [BoardCell.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/model/BoardCell.kt)
* **colorChecker** - color of checker on this cell.
* **highlighted** - if cell is highlighted.
> [Checker.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/model/Checker.kt)
* **color** - color of checker.
* **queen** - if this checker is queen.
* **pos** - position by cell name of this checker.
> [Converter.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/model/Converter.kt)
* **mapCellListX** - map with range of coordinates, and closest coordinate X of cell. 
* **mapCellListY** - map with range of coordinates, and closest coordinate Y of cell.
> [Game.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/model/Game.kt)
* **winner** - who is winner (0 - draw, 1 - 1 player, 2 - player).
* **playerTurn** - whose turn.
* **needToBeatMap** - map with checker cell name coordinates to list with cell name coordinates what's need to beat.
> [Board.kt](https://github.com/qvchoq/EnglishCheckers/blob/master/app/src/main/java/com/coursework/englishcheckers/view/Board.kt)
* **board** - map with information about all cells of board.
* **checkersOnBoard** - map with information about all checkers on board.
* **cellToLetter** - list with range of 'a' to 'h'
___