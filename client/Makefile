# Makefile for CS 181 final project game

CC		= g++
FLAGS	= -g -O -Wall -ansi -pedantic -lcurses

EXE	= player
SRC	= player.c ../shared/socklib.c ../shared/game_util.c
OBJ	= $(SRC:.c=.o)

all: clean default

default: $(EXE)

$(EXE):	$(OBJ)	
	$(CC) -o $(EXE) $(FLAGS) $(OBJ)

clean:
	rm -f $(EXE) core *.o ../shared/*.o
