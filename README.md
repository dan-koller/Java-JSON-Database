# Java JSON Database

A client-server based database that handles and stores data in a JSON format. The original idea was based on a project
on a Jetbrains Academy track. I took this further and enhanced it by styling the input and output format. I also added
the ability to specify the local database path.

## Installation

Make sure to a [JVM](https://openjdk.java.net/install/) installed on your system. After that, download the provided
package containing:

- `client.jar`
- `server.jar`
- _config files_

1. Start the server by launching* the `launch-server.sh`** file or running it from the commandline:

```shell
java -jar /path/to/your/jar/server.jar
```

2. Start the `client.jar` from the commandline and pass the requested arguments. For example:

```shell
java -jar /path/to/your/jar/client.jar set -k "some key" -v "some value"
```

_*) On the first server start you will be prompted to specify the path to your local database file. Please enter the
absolute file path (e.g. `/path/to/your/db.json`) then restart your server._

_If you want to specify a new path, just remove the path in the `app.config` located in the source directory of your app
file._

_**) Windows users may want to replace the file ending with *.bat._

### Launcher

There are scripts available for launching the server and client from the commandline inside the lib directory. If you
build the project yourself (Gradle recommended), you may want to update the path to your jar files inside these scripts
accordingly.

If you download the provided package you don't need to change any settings.

### CLI arguments

| Option            | Description                            |
|:------------------|:---------------------------------------|
| -t, --type        | Type of the request (set, get, delete) |
| -k, --key         | Record key                             |
| -v, --value       | Value to add                           |
| -in, --input-file | File containing the request as JSON*   |

_*) You can pass a JSON file by running the `client.jar` and pass `-in /path/to/your/file.json`_

## Features

- 🥸 Human-readable data (should not be used for clear sensitive data)
- 🖥 Multithreading (The server can handle multiple clients and request at a time)
- ⚡️ Lightweight and fast