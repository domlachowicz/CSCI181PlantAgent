/* game_util.h
 * -----------
 * CS 181
 *
 * Utility code for the final project game
 */

#ifndef _GAME_UTIL_H_
#define _GAME_UTIL_H_

/* IMAGE_SIZE
 * ----------
 * Dimension of plant images 
 */
#define IMAGE_SIZE 6

/* command_t
 * ---------
 * Commands the player can send to the game
 */
typedef enum {
  MOVE_UP,
  MOVE_LEFT,
  MOVE_DOWN,
  MOVE_RIGHT,
  EAT_PLANT,
  GET_PLANT_TYPE,
  GET_PLANT_IMAGE,
  GET_LIFE,
  GET_X,
  GET_Y,
  GET_ROUND,
  GET_STARTING_LIFE,
  GET_PLANT_BONUS,
  GET_PLANT_PENALTY,
  ALIVE,
  END_GAME
} command_t;

/* eat_plant_t
 * -----------
 * Result of eating a plant
 */
typedef enum {
  NO_PLANT_TO_EAT,
  PLANT_ALREADY_EATEN,
  EAT_NUTRITIOUS_PLANT,
  EAT_POISONOUS_PLANT
} eat_plant_t;

/* plant_t
 * -------
 * Type of plant in a square
 */
typedef enum {
  NO_PLANT,
  UNKNOWN_SQUARE,
  UNKNOWN_PLANT,
  NUTRITIOUS_PLANT,
  POISONOUS_PLANT
} plant_t;

/* Function Prototypes */
int move_up(int sock);
int move_left(int sock);
int move_down(int sock);
int move_right(int sock);
eat_plant_t eat_plant(int sock);
plant_t get_plant_type(int sock);
int get_plant_image(int sock, int image[IMAGE_SIZE][IMAGE_SIZE]);
int get_life(int sock);
int getx(int sock);
int gety(int sock);
int get_round(int sock);
int get_starting_life(int sock);
int get_plant_bonus(int sock);
int get_plant_penalty(int sock);
int alive(int sock);
void end_game(int sock);
int write_and_read_int(int sock, int data);
int write_int(int sock, int data);
int read_int(int sock);

#endif
