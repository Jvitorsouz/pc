#!/bin/bash

# generate $nfiles large files in a dataset directory
nfiles=$1

# File size in megabytes
file_size_mb=10

# Directory for the dataset
dir="dataset"
# Name of the single source file
template_file="$dir/template_file"

mkdir -p $dir

echo "--- Gerando arquivo molde de ${file_size_mb}MB ---"
# 1. Crie o arquivo molde APENAS UMA VEZ
dd if=/dev/urandom of=$template_file bs=1M count=$file_size_mb status=progress

echo -e "\n--- Copiando o arquivo molde ${nfiles} vezes ---"
# 2. Copie o arquivo molde para criar os arquivos finais
for i in $(seq 1 $nfiles); do
    cp $template_file $dir/file.$i
    echo "Criado: $dir/file.$i"
done

echo -e "\nProcesso concluído!"
