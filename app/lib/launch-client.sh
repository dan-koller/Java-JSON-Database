# Concatenate arguments string to match input format
type="${1} ${2}"
key="${3} ${4}"
value="${5} ${6}"

# Please update the path to your local system accordingly
FILE_PATH="./client/client.jar"

# Launch database client
java -jar $FILE_PATH "$type" "$key" "$value"