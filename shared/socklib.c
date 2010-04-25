/* socklib.c
 * ---------
 * Socket Library
 */

#include "socklib.h"

/* open_server_sock
 * ----------------
 * Opens a server socket
 * Returns the socket on success; -1 on error
 */
int open_server_sock(int port) {
  int sock;
  int on = 1;
  struct sockaddr_in addr;

  /* Ignore Sigpipe */
  signal(SIGPIPE, SIG_IGN);

  /* Open a socket */
  sock = socket(AF_INET, SOCK_STREAM, 0);
  if(sock < 0) {
    perror("open_server_sock: error opening socket");
	return -1;
  }

  /* Set the REUSE_PORT socket option */
  if(setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, 
  (char *) &on, sizeof(on)) < 0) {
    perror("setsockopt");
  }    

  /* Initialize the socket address struct */
  bzero((char *) &addr, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);

  /* Bind the socket to the specified port */
  if(bind(sock, (struct sockaddr*) &addr, sizeof(addr)) < 0) {
    perror("open_server_sock: bind");
	return -1;
  }

  /* Listen on the socket for connections */
  listen(sock, SOMAXCONN);

  return sock;
}

/* accept_connection
 * -----------------
 * Accepts a connection from a client
 * Returns the client socket on success; -1 on error
 */
int accept_connection(int sock) {
  int client;
  socklen_t addr_len;
  struct sockaddr_in addr;

  /* Get the size of the socket address struct */
  addr_len = sizeof(addr);

  /* Accept a connection */
  client = accept(sock, (struct sockaddr *) &addr, &addr_len);
  if(client < 0) {
    perror("accept_connection: accept failed");
	return -1;
  }

  return client;
}

/* open_client_sock 
 * ----------------
 * Opens a client socket
 * Returns the socket on success; -1 on error
 */
int open_client_sock(char *hostname, int port) {
  int sock;
  struct hostent *hp;
  struct sockaddr_in addr;

  /* Open a socket */
  sock = socket(AF_INET, SOCK_STREAM, 0);
  if(sock < 0) {
    perror("open_client_sock: error opening socket");
	return -1;
  }

  /* Get the host address */
  hp = gethostbyname(hostname);
  if(hp == NULL) {
    fprintf(stderr, "open_client_sock: unknown host `%s'\n", hostname);
	return -1;
  }

  /* Initialize the socket address struct */
  bzero((char *) &addr, sizeof(addr));
  bcopy((char *) hp->h_addr, (char *) &addr.sin_addr, hp->h_length);
  addr.sin_family = AF_INET;
  addr.sin_port = htons(port);

  /* Connect to the server */
  if(connect(sock, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
    perror("open_client_sock: can't connect");
    return -1;
  }

  return sock;
}

/* close_sock
 * ----------
 * Close a socket
 */
void close_sock(int sock) {
  if(close(sock) != 0) {
    perror ("close_sock: close failed");
  }
}

/* read_data
 * ---------
 * Tries to read len bytes of data from the socket into the buffer
 * Returns the number of bytes read on success
 * If timewait seconds elapse first, returns -2
 * Returns -1 on error
 * NOTE: Timeout will not occur if timewait is negative
 */
int read_data(int sock, void *buff, int len, int timewait) {
  struct timeval timeout;
  fd_set fd_read;
  int rc;

  if(timewait >= 0) {
    /* Set up the timeval struct */
    timeout.tv_sec = timewait;
    timeout.tv_usec = 0;

    /* Wait for data on the socket */
    FD_ZERO(&fd_read);
    FD_SET(sock, &fd_read);
    rc = select(sock + 1, &fd_read, NULL, NULL, &timeout);

    /* Check for error */
    if(rc < 0) {
	  fprintf(stderr, "read_data: error or select\n");
      return -1;

    /* Check for timeout */
    } else if(!FD_ISSET(sock, &fd_read)) {
	  fprintf(stderr, "read_data: timeout on read\n");
      return -2;
    }
  }
  
  /* Try to read data */
  while((rc = read(sock, buff, len)) < 0) {
    /* Check for interrupt */
	if(errno == EINTR) {
	  /* Try again */
	  continue;

	/* Signal an error */
	} else {
	  perror("read_data: error on read");
	  return -1;
	}
  }

  return rc;
}

/* write_data
 * ----------
 * Write len bytes of data from the buffer to the socket
 * Returns the number of bytes written on success; -1 on error
 */
int write_data(int sock, void *buff, int len) {
  int rc;

  while((rc = write(sock, buff, len)) < 0) {
    /* Check for interrupt */
	if(errno == EINTR) {
	  /* Try again */
	  continue;

    /* Signal an error */
	} else {
	  perror("write_data: error on write");
	  return -1;
	}
  }

  return rc;
}
