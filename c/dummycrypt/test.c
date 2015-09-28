#include "easycrypt.h"
#include <string.h>
#define DEFAULT_KEY_LENGTH 10

int main(int argc, char* argv[]){
    
    char *file_input;
    char *file_output;
    char *key;
    

    if( argc < 4 ){
        printf("Usage: -[e/d] file_in file_out key\n");
        return -1;
    }

    file_input = argv[2];
    file_output = argv[3];
    
    if( argc == 4 )
        ec_generate_key(&key, DEFAULT_KEY_LENGTH);
    else
        key = argv[4];

    if( strcmp(argv[1], "-e") == 0 ){    
        printf("Encrypting file...\n");
        ec_encrypt_file(file_input, file_output, key);
        printf("It's recommended to save the key\n");
        printf("%s\n", key);
    } else { 
        printf("Decrypting file...\n");
        ec_decrypt_file(file_input, file_output, key);
    }
}   
