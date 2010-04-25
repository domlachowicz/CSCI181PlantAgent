/* game_util.c
 * -----------
 * CS 181
 *
 * Utility code for the final project game
 */

#include "game_util.h"
#include "socklib.h"

/* move_up
 * -------
 * Move UP one tile
 * Returns 0 on success, -1 on error
 */
int move_up(int sock) {
  return write_int(sock, MOVE_UP);
}

/* move_left
 * ---------
 * Move LEFT one tile
 * Returns 0 on success, -1 on error
 */
int move_left(int sock) {
  return write_int(sock, MOVE_LEFT);
}

/* move_down
 * ---------
 * Move DONW one tile
 * Returns 0 on success, -1 on error
 */
int move_down(int sock) {
  return write_int(sock, MOVE_DOWN);
}

/* move_right
 * ----------
 * Move RIGHT one tile
 * Returns 0 on success, -1 on error
 */
int move_right(int sock) {
  return write_int(sock, MOVE_RIGHT);
}



/* eat_plant
 * ---------
 * Eat the plant at the current location
 * Returns the result on success; -1 on failure
 */
eat_plant_t eat_plant(int sock) {
  return (eat_plant_t) write_and_read_int(sock, EAT_PLANT);
}

/* get_plant_type
 * --------------
 * Get the type of the plant at the current location
 * Returns the result on success; -1 on failure
 */
plant_t get_plant_type(int sock) {
  return (plant_t) write_and_read_int(sock, GET_PLANT_TYPE);
}

/* get_plant_image
 * ---------------
 * Get the image of the plant at the current location
 * Returns 0 on success; -1 on failure
 */
int get_plant_image(int sock, int image[IMAGE_SIZE][IMAGE_SIZE]) {
  int row, col;

  write_int(sock, GET_PLANT_IMAGE);
  for(row = 0; row < IMAGE_SIZE; row++) {
    for(col = 0; col < IMAGE_SIZE; col++) {
	  if((image[row][col] = read_int(sock)) < 0) {
	    return -1;
	  }
	}
  }

  return 0;
}

/* get_life
 * --------
 * Get the player's current life count
 * Returns the result on success; -1 on failure
 */
int get_life(int sock) {
  return write_and_read_int(sock, GET_LIFE);
}

/* getx
 * ----
 * Get the x-coordinate of the player's current location
 * Returns the result on success; -1 on failure
 */
int getx(int sock) {
  return write_and_read_int(sock, GET_X);
}

/* gety
 * ----
 * Get the y-coordinate of the player's current location
 * Returns the result on success; -1 on failure
 */
int gety(int sock) {
  return write_and_read_int(sock, GET_Y);
}

/* get_round
 * ---------
 * Get the current round of the game
 */
int get_round(int sock) {
  return write_and_read_int(sock, GET_ROUND);
}

/* get_starting_life
 * -----------------
 * Get the player's  starting life
 */
int get_starting_life(int sock) {
  return write_and_read_int(sock, GET_STARTING_LIFE);
}

/* get_plant_bonus
 * ---------------
 * Get the life bonus for eating a nutritious plant
 */
int get_plant_bonus(int sock) {
  return write_and_read_int(sock, GET_PLANT_BONUS);
}

/* get_plant_penalty
 * -----------------
 * Get the life penalty for eating a poisonous plant
 */
int get_plant_penalty(int sock) {
  return write_and_read_int(sock, GET_PLANT_PENALTY);
}

/* alive
 * -----
 * Check whether the player is alive
 * Returns the result on success; -1 on failure
 */
int alive(int sock) {
  return write_and_read_int(sock, ALIVE);
}

/* end_game 
 * --------
 * End the current game 
 */
void end_game(int sock) {
  write_int(sock, END_GAME);
}

/* write_and_read_int
 * ------------------
 * Writes and reads and integer from the given socket
 * Returns the result on success; -1 on failure
 */
int write_and_read_int(int sock, int data) {
  if(write_int(sock, data) < 0) {
    return -1;
  }

  return read_int(sock);
}

/* write_int
 * ---------
 * Write an integer from the given socket
 * Returns 0 on success; -1 on error
 */
int write_int(int sock, int data) {
  return write_data(sock, &data, sizeof(data));
}

/* read_int
 * --------
 * Read an integer from the given socket
 * Returns the result on success
 * Returns -1 on failure
 */
int read_int(int sock) {
  int rc, data;
  
  if((rc = read_data(sock, &data, sizeof(data), -1)) < 0) {
    return rc;
  }

  return data;
}
