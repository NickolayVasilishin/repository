#include"easycrypt.h"

int ec_encrypt_file(char *cfi, char *cfo, char *cp){
    
    int  c,
         count;
    FILE *fi,
         *fo;
    char *key;
    
    if( (cfi == NULL) || (cfo == NULL) )
        return -1;
    
    if( (fi = fopen(cfi, "rb")) == NULL )
        return -1;

    if( (fo = fopen(cfo, "wb")) == NULL)
        return -1;

    if( !(key = cp) || (*key=='\0') )
        return -1;
    
    count = 0;

    while ( (c = getc(fi)) != EOF ){
        if( !*key )
            key = cp;
        
        c ^= *(key++);
        putc(c, fo);
        count++;
    }

    fclose(fo);
    fclose(fi);
    
    return count;
}

int ec_decrypt_file(char *cfi, char *cfo, char *cp){
    return ec_encrypt_file(cfi, cfo, cp);
}

int ec_encrypt_string(char *string, char *cp){
    
    char *key;
    int count;

    if( string == NULL )
        return -1;

    if( !(key = cp) || (*key == '\0') )
        return -1;
    
    count = 0;

    while( *string != '\0' ){
        
        if( !*key )
            key = cp;

        *string++ ^= *key++;
        count++;    
    }

    return count;
}

int ec_decrypt_string(char *string, char *cp){
    return ec_encrypt_string(string, cp);
}

int ec_generate_key(char **key, int length){
   
    char *k;
    if( (k = (char *) malloc(length)) == NULL )
        return -1;

    srand(time(NULL));
    int i = 0;
    int s = 0;
    for( ; i < length; i++){
        s = rand()%(122-97) + 97; 
        *(k++) = s; 
    }
    *key = k - i;

    return i;
}
