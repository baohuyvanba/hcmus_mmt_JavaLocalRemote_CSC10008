# Remote Desktop Application
This is a Java-based remote desktop application, using the Client-Server model, allowing users to control a computer remotely over a local network. The application leverages Socket for communication and JavaFX for building the user interface.
****
## Information
- Course: HCMUS - Computer Network (MMT)
- Project Name: Remote computer control program
- Instructor: Do Hoang Cuong
- Group: 3 members
- Name of participating members:
  + Van Ba Bao Huy
  + Phung Doan Khoi
  + Nguyen Ta Bao

## Key Features:
- Process Management: List running processes, start/stop a specific process.
- Application Management: List running applications, start/stop a specific application.
- Screenshot: Capture screenshots of the remote computer's screen.
- Keylogger: Record keystrokes on the remote computer.
- File Management: ViewRegistry Editing**: View and edit the Windows Registry on the remote computer.

## Technical Details:
- Programming Language: Java
- Communication Protocol: TCP/IP using Sockets
- UI Framework: JavaFX
- Additional Libraries: JNA (Java Native Access) for accessing native Windows functions.

## Installation and Usage:
### Prerequisites:
- Java Development Kit (JDK) 8 or later
- JavaFX library
- JNA library
### Build:
- Clone this repository.
- Compile the Java source code using your preferred IDE or the command line.
### Run:
- Start the server application on the computer you want to control.
- Start the client application on the computer you want to control from.
- Enter the server's IP address and port in the client application.
- Connect and start using the remote desktop features.
### Security Considerations:
- This application is intended for educational and personal use only.
- Use caution when allowing remote access to your computer.
- Consider using encryption for added security (not implemented in this version).
### Disclaimer:
- This software is provided "as is" without any warranty.
- Use at your own risk.
- The developers is not responsible for any damages caused by the use of this software.
