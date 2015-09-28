#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#define DEFAULT_KEY "30aba6fdce354b78acbe73cdb38cabdacab8387207433"

//Encrypts input file and saves as output file.
//Takes file input, output names, 
//key char pointer as params.
int ec_encrypt_file(char *cfi, char *cfo, char *cp);

//Decrypts similarly
int ec_decrypt_file(char *cfi, char *cfo, char *cp);

//Encrypt arrays.
//Takes string as char pointer.
int ec_encrypt_string(char *string, char *cp);

int ec_decrypt_string(char *string, char *cp);

int ec_generate_key(char **key, int length);

//Try to extract message without a key
int ec_analyse_file(char *cfi, char *cfo);
int ec_analyse_string(char *string);
