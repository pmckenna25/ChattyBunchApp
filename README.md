# ChattyBunchApp
### Small UDP chat application

      - Port number and connection address are hardcoded into the ClientWindow.java (line 45).
      
      - Port number of the server address has been hardcoded into ChatServer.java (line 8).
      
      - Currently set up to run within your own localhost, any clients from outside will need to replace
        localhost in ClientWindow.java to the IP address of where the server is running.
        
      - Additionally port forwarding on home routers will need to be required since it is UDP.
        
### Early days project

      - Disclaimer: This was an early days project, the only updates I have made recently are to this
        Readme file. Hardcoding connection parameters is not good practise in general. I may take the
        time to update this in the future and that may also involve a change in protocol.
