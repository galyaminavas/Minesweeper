package minesweeper

import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt

class Cell() {
    var cellChar = '.'
    var marked: Boolean = false
    var opened: Boolean = false
}

class GameField(val mines: Int) {
    var field = initEmptyField()

    var userIsDead = false

    fun initEmptyField(): ArrayList<Array<Cell>> {
        val field = ArrayList<Array<Cell>>()

        for (i in 0 until 9) {
            field.add(Array<Cell>(9) {i -> Cell()})
        }
        return field
    }

    fun placeMines(mines: Int): ArrayList<Array<Cell>> {
        val field = initEmptyField()

        for (i in 1..mines) {
            var x = nextInt(from = 0, until = 9)
            var y = nextInt(from = 0, until = 9)
            while (field[y][x].cellChar != '.') {
                x = nextInt(from = 0, until = 9)
                y = nextInt(from = 0, until = 9)
            }
            field[y][x].cellChar = 'X'
        }

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (field[j][i].cellChar != 'X') {
                    var minesCount = 0
                    if (j - 1 in 0 until 9) {
                        if (i - 1 in 0 until 9) {
                            if (field[j - 1][i - 1].cellChar == 'X')
                                minesCount++
                        }
                        if (field[j - 1][i].cellChar == 'X')
                            minesCount++
                        if (i + 1 in 0 until 9) {
                            if (field[j - 1][i + 1].cellChar == 'X')
                                minesCount++
                        }
                    }
                    if (j + 1 in 0 until 9) {
                        if (i - 1 in 0 until 9) {
                            if (field[j + 1][i - 1].cellChar == 'X')
                                minesCount++
                        }
                        if (field[j + 1][i].cellChar == 'X')
                            minesCount++
                        if (i + 1 in 0 until 9) {
                            if (field[j + 1][i + 1].cellChar == 'X')
                                minesCount++
                        }
                    }
                    if (i - 1 in 0 until 9 && field[j][i - 1].cellChar == 'X')
                        minesCount++
                    if (i + 1 in 0 until 9 && field[j][i + 1].cellChar == 'X')
                        minesCount++

                    if (minesCount in 1..8)
                        field[j][i].cellChar = minesCount.toString().first()
                }
            }
        }
        return field
    }

    fun placeMinesFirstMove(mines: Int, xInit: Int, yInit: Int): ArrayList<Array<Cell>> {
        for (i in 1..mines) {
            var x = nextInt(from = 0, until = 9)
            var y = nextInt(from = 0, until = 9)
            while (field[y][x].cellChar == 'X' || (y == yInit && x == xInit)) {
                x = nextInt(from = 0, until = 9)
                y = nextInt(from = 0, until = 9)
            }
            field[y][x].cellChar = 'X'
        }

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (field[j][i].cellChar != 'X') {
                    var minesCount = 0
                    if (j - 1 in 0 until 9) {
                        if (i - 1 in 0 until 9) {
                            if (field[j - 1][i - 1].cellChar == 'X')
                                minesCount++
                        }
                        if (field[j - 1][i].cellChar == 'X')
                            minesCount++
                        if (i + 1 in 0 until 9) {
                            if (field[j - 1][i + 1].cellChar == 'X')
                                minesCount++
                        }
                    }
                    if (j + 1 in 0 until 9) {
                        if (i - 1 in 0 until 9) {
                            if (field[j + 1][i - 1].cellChar == 'X')
                                minesCount++
                        }
                        if (field[j + 1][i].cellChar == 'X')
                            minesCount++
                        if (i + 1 in 0 until 9) {
                            if (field[j + 1][i + 1].cellChar == 'X')
                                minesCount++
                        }
                    }
                    if (i - 1 in 0 until 9 && field[j][i - 1].cellChar == 'X')
                        minesCount++
                    if (i + 1 in 0 until 9 && field[j][i + 1].cellChar == 'X')
                        minesCount++

                    if (minesCount in 1..8)
                        field[j][i].cellChar = minesCount.toString().first()
                }
            }
        }
        openCell(xInit, yInit)
        return field
    }

    fun printWithoutMines() {
        println(" |123456789|")
        println("—|—————————|")
        for (i in field.indices) {
            print("${i + 1}|")
            for (j in field[i].indices) {
                if (field[i][j].cellChar == 'X')
                    print(if (field[i][j].marked) '*' else '.')
                else print(if (field[i][j].marked) '*'
                           else field[i][j].cellChar)
            }
            println("|")
        }
        println("—|—————————|")
    }

    fun printFinal() {
        println(" |123456789|")
        println("—|—————————|")
        for (i in field.indices) {
            print("${i + 1}|")
            for (j in field[i].indices) {
                if (field[i][j].opened) {
                    if (field[i][j].cellChar == '.')
                        print('/')
                    else
                        print(field[i][j].cellChar)
                } else if (field[i][j].marked)
                    print('*')
                else
                    print('.')
            }
            println("|")
        }
        println("—|—————————|")
    }

    fun gameIsOverAllMarked(): Boolean {
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j].cellChar == 'X' && !field[i][j].marked ||
                        field[i][j].cellChar != 'X' && field[i][j].marked)
                    return false
            }
        }
        return true
    }

    fun gameIsOverAllOpened(): Boolean {
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j].cellChar != 'X' && !field[i][j].opened)
                    return false
            }
        }
        return true
    }

    fun openCell(x: Int, y: Int) {
        if (field[y][x].cellChar == 'X') {
            userIsDead = true
            printDead()
            println("You stepped on a mine and failed!")
        } else if (field[y][x].cellChar == '.') {
            field[y][x].opened = true
            field[y][x].marked = false
            if (y - 1 in 0 until 9) {
                if (x - 1 in 0 until 9) {
                    if (!field[y - 1][x - 1].opened)
                        openCell(x - 1, y - 1)
                }
                if (!field[y - 1][x].opened)
                    openCell(x, y - 1)
                if (x + 1 in 0 until 9) {
                    if (!field[y - 1][x + 1].opened)
                        openCell(x + 1, y - 1)
                }
            }
            if (y + 1 in 0 until 9) {
                if (x - 1 in 0 until 9) {
                    if (!field[y + 1][x - 1].opened)
                        openCell(x - 1, y + 1)
                }
                if (!field[y + 1][x].opened)
                    openCell(x, y + 1)
                if (x + 1 in 0 until 9) {
                    if (!field[y + 1][x + 1].opened)
                        openCell(x + 1, y + 1)
                }
            }
            if (x - 1 in 0 until 9 && !field[y][x - 1].opened)
                openCell(x - 1, y)
            if (x + 1 in 0 until 9 && !field[y][x + 1].opened)
                openCell(x + 1, y)

        } else {
            field[y][x].opened = true
        }
    }

    fun printDead() {
        println(" |123456789|")
        println("—|—————————|")
        for (i in field.indices) {
            print("${i + 1}|")
            for (j in field[i].indices) {
                if (field[i][j].opened && field[i][j].cellChar == '.') {
                    print('/')
                } else if (field[i][j].cellChar == '.' && field[i][j].marked) {
                    print('*')
                } else {
                    print(field[i][j].cellChar)
                }
            }
            println("|")
        }
        println("—|—————————|")
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    val minesNumber = scanner.nextInt()
    val field = GameField(minesNumber)
    field.printFinal()

    var xInit = 0
    var yInit = 0
    var commandInit = ""
    while (commandInit != "free") {
        print("Set/unset mines marks or claim a cell as free: ")
        xInit = scanner.nextInt()
        yInit = scanner.nextInt()
        commandInit = scanner.next()
        if (commandInit == "mine") {
            field.field[yInit - 1][xInit - 1].marked = !field.field[yInit - 1][xInit - 1].marked
            field.printFinal()
            continue
        } else if (commandInit != "free") {
            println("Unknown command.")
            continue
        }
    }
    field.field = field.placeMinesFirstMove(minesNumber, xInit - 1, yInit - 1)
    field.printFinal()

    while (!field.gameIsOverAllMarked() && !field.userIsDead && !field.gameIsOverAllOpened()) {
        print("Set/unset mines marks or claim a cell as free: ")
        val x = scanner.nextInt()
        val y = scanner.nextInt()
        val command = scanner.next()
        if (command == "mine") {
            if (field.field[y - 1][x - 1].opened) {
                println("The cell is already opened.")
                continue
            } else {
                field.field[y - 1][x - 1].marked = !field.field[y - 1][x - 1].marked
                field.printFinal()
                continue
            }
        } else if (command == "free") {
            if (field.field[y - 1][x - 1].opened) {
                println("The cell is already opened.")
                continue
            } else if (field.field[y - 1][x - 1].marked) {
                println("Remove mine mark from the cell before open.")
                continue
            } else {
                field.openCell(x - 1, y - 1)
                if (!field.userIsDead)
                    field.printFinal()
                continue
            }
        } else {
            println("Unknown command.")
            continue
        }
    }
    if (!field.userIsDead)
        println("Congratulations! You found all mines!")
}
