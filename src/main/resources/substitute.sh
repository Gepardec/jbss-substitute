#!/bin/bash

# Substitute script

MY_PATH=$(readlink -f $0)
BIN_DIR=`dirname $MY_PATH`
BASE_DIR=$BIN_DIR/..
CONFIG_DIR=$BASE_DIR
PRG=`basename $0`

SAVE_DATE=`date +save_%Y_%m_%d-%H_%M_%S`
RM_ACTION=manual

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2

usage: $PRG -p properties_file -t template_file -o output_file

Funktion:
	Transforms the given freemarker template using properties from properties file and writes result to file

EOF
}

######################   Optionen bestimmen ###################

while getopts "p:t:o:" option
do
    case $option in
      p)
        PROP_FILE=$OPTARG;;
      t)
        TEMPLATE_FILE=$OPTARG;;
      o)
        OUTPUT_FILE=$OPTARG;;
      *)
        print_usage
        exit 1
        ;;
    esac
done

shift `expr $OPTIND - 1`


##################### Beginn #########################

if [ x$PROP_FILE = x ]
then
	echo "Property file $PROP_FILE not found"
	print_usage
	exit 1
fi

if [ ! -f $TEMPLATE_FILE ] || [ x$TEMPLATE_FILE = x ]
then
	echo "Template file $TEMPLATE_FILE not found"
	exit 1
fi
java -jar $BIN_DIR/substitute.jar $PROP_FILE $TEMPLATE_FILE $OUTPUT_FILE
