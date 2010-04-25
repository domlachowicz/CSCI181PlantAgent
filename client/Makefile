# Makefile for CS 181 final project game

CC		= g++
CFLAGS	= -g -O0 -Wall -ansi -pedantic
LDFLAGS = -lcurses
FLAGS=$(CFLAGS) $(LDFLAGS)

EXE	= player
EXE_SRC	= player.c ../shared/socklib.c ../shared/game_util.c
EXE_OBJ	= $(EXE_SRC:.c=.o)
DOWNLOADER = downloader
DOWNLOADER_SRC	= downloader.c ../shared/socklib.c ../shared/game_util.c
DOWNLOADER_OBJ	= $(DOWNLOADER_SRC:.c=.o)
READER = reader
READER_SRC	= reader.c
READER_OBJ	= $(READER_SRC:.c=.o)

all: clean default

default: $(EXE) $(DOWNLOADER) $(BOARD) $(READER)

$(EXE):	$(EXE_OBJ)	
	$(CC) -o $(EXE) $(FLAGS) $(EXE_OBJ)

$(DOWNLOADER):	$(DOWNLOADER_OBJ)	
	$(CC) -o $(DOWNLOADER) $(FLAGS) $(DOWNLOADER_OBJ)

$(BOARD):	$(BOARD_OBJ)	
	$(CC) -o $(BOARD) $(FLAGS) $(BOARD_OBJ)

$(READER):	$(READER_OBJ)	
	$(CC) -o $(READER) $(CFLAGS) $(READER_OBJ)

clean:
	rm -f $(EXE) $(DOWNLOADER) $(BOARD) $(READER) *~ core *.o ../shared/*.o
