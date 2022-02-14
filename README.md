The scripts were made with groovy version 3.0.9 JVM 16.0.1
I have no idea about backwards compatiblity as I don't spend that much time coding groovy. (This is something I regret)
Please have installed at least that version or higher.

There are two scripts in the repo:
  1. jb-postman.groovy : Converts an .http file into a collection.json file,
  2. jb-env-postman.groovy : Converts a jetbrains environment file into a postman environment file.

Usage 1):

    groovy jb-postman.grooy ./my-file.http ./my-converted-file.json

This will convert the http file into a postman json collection using the second argument for the output path and first for the input file path. The output file need not be created. If it already exists it will be deleted and recreated.

Usage 2):

    groovy jb-env-postman.groovy ./my-file.env.json

This will convert the jb env file into several postman environment json files. It will create as many files as there are environments in the jb file. Each will be created in the path of the scripts execution and will be named according to the environment name (with the .json suffix).

Please flag any issues you find. This has only been tested on my limited use cases.

If you find it interesting, please give a star.

I would like to see this functionality as a JetBrains plugin but I don't have knowledge about making them.
If anyone would like to collaborate on something like that, feel free to contact me here.