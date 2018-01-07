​There are some important remarks to be made that are either too extensive to be implemented as comments in the code itself or too technical to be mentioned in the portfolio report. Therefore, we will continue our discussion of the code in this readme.md file. This text also includes much of the comments that can be found in the code as well.

# class: FileDiscovery
## method: discoverFiles() 
Scans every locally present file on the node during setup. 

## method: fileCheck()
This check is executed when a new local file appears. It is used to determine who the owner is en where a replica needs to be transferred to. Here we use the `fileInformation` class to determine the replication destination. During the making of the project we adjusted this method by adding `Bestandsfiche` objects which show some overlap with `fileInformation` when it comes to functionality. Adding these new objects was easier too implement then extending the `fileInformation` class, which was never really designed to be extendable in the first place.

## method: fileCheckDownloads()
This method, unfortunately, has a rather badly chosen name, which we only realised after it was almost practically impossible too change the name without the risk of braking the code with it. This method is called whenever the node receives replication files, and so, not when downloading. Though, 'replication' technically speaking also encompasses the 'downloading' of a file, we decided to make a distinction between the two (after the method name was already chosen, that is).

## method: fileCheckNewNode()
Here we determine what has to change when it comes to replication locations and owners whenever a new `previous` node is added.

## method: fileCheckShutdownNode()
Called before the node shuts itself down. It determines which files need to be duplicated, where they need to be duplicated to or whether they need to be deleted or who will become the new owner of the file. This is method is not used in our project, as it got replaced with a newer version (see `fileCheckShutdownNodev2()` below). The original version was only kept in the code just in case v2 would turn out to be even more of a nuissance than it's predecessor. 

## method: fileCheckShutdownNodev2()
This method implements the exact same functionality as `fileCheckShutdownNode()` but in a different and updated way. 

## method: regelReplicaties()
This method handles the transfer and modification of replicas on other nodes. This method is always called from a remote node through RMI and thus, is never called locally.

## method: regelLokale()
This method handles modifications of local files but is also never called locally. This method is remotely called with RMI from another node.

## method: getIO()
This method was required in order to enable IO functionality in other classes (classes that did have a `fileDiscovery` instance)

## method: sendBestandsFiche()
Used to send `bestandsfiche` objects to a new owner whenever ownership changes occur

## method: send()
sends a given file to a specified node on the network
