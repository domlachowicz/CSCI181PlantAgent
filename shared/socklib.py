from socket import *
import signal
import struct

def open_server_sock(port):
    on = 1
    #ignore SIGPIPE
    signal.signal(signal.SIGPIPE, signal.SIG_IGN)
        
    #Open a socket
    sock = socket(AF_INET, SOCK_STREAM)
      
    #Set the REUSE_PORT socket option
    sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, on)
    #Initialize the socket address struct
    

    #Bind the socket to the specific port
    sock.bind((gethostname(), port))

    #Listen to the socket for connections
    sock.listen(SOMAXCONN)
    return sock

def accept_connection(sock):
    client, addr = sock.accept()
    return client


def open_client_sock(hostname, port):
    sock = socket(AF_INET, SOCK_STREAM)
    hp = gethostbyname(hostname)
    
    if hp == None:
        print "open_client_sock: unknown"
        return None
    else:
        sock.connect((hp, port))
        return sock


    


def close_sock(sock):
    sock.close()


def read_data(sock, length):
    sockfile = sock.makefile()
    buff = sockfile.read(length)
    sockfile.flush()
    return buff
    

def write_data(sock, data):
    sockfile = sock.makefile()
    sockfile.write(data)
    sockfile.flush()




    

