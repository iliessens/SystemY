# SystemY
5-Distributed Systems  
**Group 3**

Jannes Van Goeye  
Robbe Pauwels  
Imre Liessens  

## Versions
A number of branches have been created to keep the different versions apart.  

* PR4-discovery  
* PR5-replication: bootstrap and replication, no shutdown or failure  
* PR5.2-Replication: full PR5 assignment  
* PR6-Agents: working FileListAgent + inactive FailureAgent
* PR7-GUI: Untested GUI  
* Master: Same as PR7 + portfolio's  

###Important notes  

⚠️ **The most recent stable version is PR6-Agents.** Use this for testing the system. ⚠️  

🛑 **Note:** the System is untested on more than three hosts: one namingserver and two nodes. Using more nodes is at your own risk.  

## How to run the project  
This manual will guide you through the setup for running the project in the IntelliJ IDE. Running form CLI is possible and very similar.  

1. Open IntelliJ  
2. Open the project trough File -> New -> New Project from existing sources  
	Note that you need to do this two times: once for the naming server and once for the node-project.

2. Go to Run > Edit configurations  

      If there is nothing shown: run the main class first to create a configuration automatically. Do this by opening the main class and clicking the small green arrow next to the line numbers.
3. Enter your preferred arguments in the box "Program Arguments".  

    **For Node**  
    The format is ``name IP-address``  
    For example: ``imre 10.2.1.10``. The IP address indicates the interface on which the program will bind. Make sure it is correctly configured.  

    **For nameserver**  
    Enter your preferred IP-address in the box.  
    When the IP-address is not bound to any interface on the host an error will be shown during program startup.

4. Click the OK button
5. Add any files you want in the network to the following folder ```.../node/files/original/```
5. Click the run button in the upper right corner.

If a message of the *firewall* appears, allow it. If the system won't work, it might be necessary to disable the firewall entirely.

Start the nameserver first and then the nodes. Debugging output will be sent to the console, as are any exceptions.

**Note:** Unfortunately it is not possible to run a node on the same host as the nameserver

**Note for running on OS X:** We experienced problems running the system on a MAC OS computer. These were caused by the incorrect routing of packets when two network connections were present (WiFi and wired). The easiest solution to this is to disable the wireless adapter from the task bar.
