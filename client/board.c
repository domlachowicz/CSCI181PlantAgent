// board.c
// --------
// CS 181
// 
// Example player for the final project

#include "../shared/socklib.h"
#include "../shared/game_util.h"
#include <string.h>
#include <stdlib.h>
#include <iostream>

using namespace std;

// Function Prototypes
int parse_input (int argc, char **argv, int &port);
void print_usage ();
void print_plant_image (int sock);
void print_instructions ();

#define QUADRANT 50

static char board[QUADRANT * 2][QUADRANT * 2];

static void print_board(void) {
  for (int x = 0; x < 2 * QUADRANT; x++) {
    for (int y = 0; y < 2 * QUADRANT; y++) {
      printf("%c", board[x][y]);
    }
    printf("\n");
  }
}

int
main (int argc, char **argv)
{
  atexit(&print_board);
  memset(board, 'X', sizeof(board));

  int port, sock;
  string input;

  // Parse the command line
  if (parse_input (argc, argv, port) < 0)
    {
      print_usage ();
      return 1;
    }

  // Open a socket to the game server
  // Assumes that the game server is listening on the local host
  sock = open_client_sock ("localhost", port);
  if (sock < 0)
    {
      printf ("Error connecting to game server on port %d\n", port);
      return 1;
    }

  // Loop as long as the player is alive
  while (alive (sock))
    {
      fflush(stdout);
      int move = rand () % 6;
      int x, y;

      switch (get_plant_type (sock))
	{
	case NO_PLANT:
	  break;

	case UNKNOWN_PLANT:
	  break;

	case NUTRITIOUS_PLANT:
	  //printf ("nutritious\n");
	  break;

	case POISONOUS_PLANT:
	  //printf ("poisonous\n");
	  break;
	}

      switch (move) {
      case 0:
	move_up(sock);
	break;
      case 1:
	move_down(sock);
	break;
      case 2:
      case 5:
	move_left(sock);
	break;
      case 3:
      case 4:
	move_right(sock);
	break;
      }

      x = getx(sock) + QUADRANT; 
      y = gety(sock) + QUADRANT;      

      if (x < 0 || y < 0 || x >= (2 * QUADRANT) || y >= (2 * QUADRANT)) {
	break;
      }

      switch (get_plant_type (sock))
	{
	case NUTRITIOUS_PLANT:
	  board[x][y] = 'N';
	  break;
	  
	case POISONOUS_PLANT:
	  board[x][y] = 'P';
	  break;
	default:
	  break;
	}
      
      if (board[x][y] == 'X') {

	switch (eat_plant (sock))
	  {
	  case NO_PLANT_TO_EAT:
	    board[x][y] = ' ';
	    //printf("no plant\n");
	    break;
	    
	  case PLANT_ALREADY_EATEN:
	    //printf("already eaten\n");
	    break;
	    
	  case EAT_NUTRITIOUS_PLANT:
	    board[x][y] = 'N';
	    //print_plant_image (sock);
	    //printf ("nutritious\n");
	    break;

	  case EAT_POISONOUS_PLANT:
	    board[x][y] = 'P';
	    //print_plant_image (sock);
	    //printf ("poisonous\n");
	    break;
	  }
      }
    }

  // Tell the server to end the game
  end_game (sock);
}

// parse_input
// -----------
// Parse the command line input
// Returns 0 on success; -1 on error
int
parse_input (int argc, char **argv, int &port)
{
  if (argc != 2)
    {
      return -1;
    }

  port = atoi (argv[1]);
  return 0;
}

// print_usage
// -----------
// Print proper command line usage
void
print_usage ()
{
  printf ("usage: player port\n");
}

// print_plant_image
// -----------------
// Print a plant image
void
print_plant_image (int sock)
{
  int row, col;
  int image[IMAGE_SIZE][IMAGE_SIZE];

  if (get_plant_image (sock, image) < 0)
    {
      printf ("Error getting image\n");

    }
  else
    {
      for (row = 0; row < IMAGE_SIZE; row++)
	{
	  for (col = 0; col < IMAGE_SIZE; col++)
	    {
	      printf ("%3d", image[row][col]);
	    }

	  printf ("\n");
	}
    }
}

// print_instructioAns
// ------------------
// Print instructions for the user
void
print_instructions ()
{
  printf ("Use i, j, k, l to move\n");
  printf ("Type e to eat a plant\n");
  printf ("Type o to observe a plant\n");
  printf ("Type exit to end the game\n");
  printf ("Type ? or help to view these intructions\n\n");
}
