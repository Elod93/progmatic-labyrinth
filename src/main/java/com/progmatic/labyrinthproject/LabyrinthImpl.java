package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    private int width = 0;
    private int height = 0;
    private Coordinate playerPosition;
    private CellType[][] labyrinth = new CellType[5][5];

    public LabyrinthImpl() {
        for (int i = 0; i < labyrinth.length; i++) {
            Arrays.fill(labyrinth[i], CellType.EMPTY);
        }


    }

    @Override
    public int getWidth() {
        if (width <= 0) {
            return -1;
        } else {
            return width;
        }

    }

    @Override
    public int getHeight() {
        if (height <= 0) {
            return -1;

        } else {
            return height;
        }
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            labyrinth = new CellType[width][height];
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            setCellType(new Coordinate(ww, hh), CellType.WALL);
                            break;
                        case 'E':
                            setCellType(new Coordinate(ww, hh), CellType.END);
                            break;
                        case 'S':
                            setCellType(new Coordinate(ww, hh), CellType.START);
                            break;
                        default:
                            setCellType(new Coordinate(ww, hh), CellType.EMPTY);
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException | CellException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() < 0 || c.getCol() < 0 || c.getCol() > getHeight() - 1 || c.getRow() > getWidth() - 1) {
            throw new CellException(c.getRow(), c.getCol(), "Nincs ilyen koordinata");
        }
        return labyrinth[c.getRow()][c.getCol()];

    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (type == CellType.START) {
            playerPosition = new Coordinate(c.getRow(), c.getCol());
        }
        if (c.getRow() < 0 || c.getCol() < 0 || c.getCol() > getHeight()-1 || c.getRow() > getWidth()-1) {
            throw new CellException(c.getRow(), c.getCol(), "Nincs ilyen koordinata");
        } else {
            labyrinth[c.getRow()][c.getCol()] = type;
        }

    }

    @Override
    public Coordinate getPlayerPosition() {
        for (int i = 0; i <labyrinth.length ; i++) {
            for (int j = 0; j <labyrinth[i].length ; j++) {
                    if(playerPosition.getRow()==i && playerPosition.getCol()==j){
                        return new Coordinate(j,i);
                    }

            }
        }
       return null;
    }

    @Override
    public boolean hasPlayerFinished() {
        for (int i = 0; i <labyrinth.length ; i++) {
            for (int j = 0; j <labyrinth[i].length ; j++) {
                if(labyrinth[i][j]==CellType.END){
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> directionList=new ArrayList<>();
        getPlayerPosition();

       if(playerPosition.getCol()<labyrinth[0].length &&labyrinth[playerPosition.getRow()][playerPosition.getCol()+1]==CellType.EMPTY){
           directionList.add(Direction.EAST);
       }
       if (playerPosition.getCol()>0 &&labyrinth[playerPosition.getRow()][playerPosition.getCol()-1]==CellType.EMPTY ){
           directionList.add(Direction.WEST);
       }
       if (playerPosition.getRow()<labyrinth.length && labyrinth[playerPosition.getRow()+1][playerPosition.getCol()]==CellType.EMPTY){
               directionList.add(Direction.SOUTH);

       }
       if (playerPosition.getRow()>0 && labyrinth[playerPosition.getRow()-1][playerPosition.getCol()]==CellType.EMPTY){
               directionList.add(Direction.NORTH);
       }
        return directionList;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {

        for (Direction possibleMove : possibleMoves()) {
            if(possibleMove==direction ){
                if(direction==Direction.EAST) {
                    if(playerPosition.getRow()>=width ){
                        throw new InvalidMoveException();
                    }
                    playerPosition = new Coordinate(playerPosition.getCol(), playerPosition.getRow() + 1);

                }else if(direction==Direction.WEST){
                    if (playerPosition.getRow()<0){
                        throw new InvalidMoveException();
                    }
                    playerPosition= new Coordinate(playerPosition.getCol(),playerPosition.getRow()-1);

                }else if(direction==Direction.NORTH){
                    if(playerPosition.getCol()<0){
                        throw new InvalidMoveException();
                    }
                    playerPosition= new Coordinate(playerPosition.getCol()-1,playerPosition.getRow());
                }else {
                    if(playerPosition.getCol()>=height ){
                        throw new InvalidMoveException();
                    }
                    playerPosition=new Coordinate(playerPosition.getCol()+1,playerPosition.getRow());
                }
            }
        }


    }


}
