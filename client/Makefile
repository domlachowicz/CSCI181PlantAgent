# Makefile for CS 181 final project game

CC		= g++
FLAGS	= -g -O0 -Wall -ansi -pedantic -lcurses

EXE	= player
EXE_SRC	= player.c ../shared/socklib.c ../shared/game_util.c
EXE_OBJ	= $(EXE_SRC:.c=.o)
DOWNLOADER = downloader
DOWNLOADER_SRC	= downloader.c ../shared/socklib.c ../shared/game_util.c
DOWNLOADER_OBJ	= $(DOWNLOADER_SRC:.c=.o)
BOARD = board
BOARD_SRC	= board.c ../shared/socklib.c ../shared/game_util.c
BOARD_OBJ	= $(BOARD_SRC:.c=.o)

all: clean default

default: $(EXE) $(DOWNLOADER) $(BOARD)

$(EXE):	$(EXE_OBJ)	
	$(CC) -o $(EXE) $(FLAGS) $(EXE_OBJ)

$(DOWNLOADER):	$(DOWNLOADER_OBJ)	
	$(CC) -o $(DOWNLOADER) $(FLAGS) $(DOWNLOADER_OBJ)

$(BOARD):	$(BOARD_OBJ)	
	$(CC) -o $(BOARD) $(FLAGS) $(BOARD_OBJ)

clean:
	rm -f $(EXE) $(DOWNLOADER) $(BOARD) core *.o ../shared/*.o
