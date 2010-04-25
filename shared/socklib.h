/* socklib.h
 * ---------
 * Socket Library
 */

#ifndef _SOCKLIB_H_
#define _SOCKLIB_H_

#include <netdb.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <signal.h>
#include <strings.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <netinet/in.h>

typedef uint32_t u_int32_t;

/* Function Prototypes */
int open_server_sock(int port);
int accept_connection(int sock);
int open_client_sock(char *hostname, int port);
void close_sock(int sock);
int read_data(int sock, void *buff, int len, int timewait);
int write_data(int sock, void *buff, int len);

#endif
