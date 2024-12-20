# mFTP

This Android app allows users to run an **SFTP (Secure File Transfer Protocol) Server** directly on their device. With the press of a button, you can start or stop an SFTP server, allowing file transfers over a secure connection.


## Features

- Start/Stop the SFTP server with a single button click.
- Simple and easy-to-use interface.
- Generates random passwords for secure access.
- Background service to keep the SFTP server running even when the app is not in the foreground.
- Persistent notification when the server is active.
- Compatible with Android SDK 26 and above.


## Requirements

- **Android 8.0 (API level 26)** or higher.
- **Wi-Fi connection** for serving the files over the local network.


## Libraries

This app uses the following libraries:

- **Apache Mina SSHD**: A library for implementing SSH and SFTP servers.


## How It Works

1. **Starting the SFTP Server**:
   - Upon launching the app, press the "Start Server" button.
   - The app will start an SFTP server on port `2222` with a randomly generated password.
   - The server address, username, and password will be displayed for you to connect via an SFTP client (e.g., FileZilla).

2. **Stopping the SFTP Server**:
   - Press the "Stop Server" button to stop the SFTP server.
   - Once stopped, the server will no longer accept any connections, and the notification will disappear.

3. **Persistent Background Service**:
   - The SFTP server will run in the background, even if the app is minimized.
   - A persistent notification will indicate that the server is active.
   - Stopping the service removes the notification and stops the server.


## Permissions

This app requires the following permissions:
- **Storage Permission**: To allow access to your device’s files for upload/download.
- **Wi-Fi Permission**: To allow file transfers over a local network.


## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/adf09c38-3413-4727-92ef-8437b377f800" width="30%" />
  <img src="https://github.com/user-attachments/assets/4751571a-0ff8-4762-ae24-43b198c16767" width="30%" />
</p>

## Usage

To connect to the SFTP server:
1. Open any SFTP client like **FileZilla**, **WinSCP**, etc.
2. Enter the **server address** and **port** displayed in the app.
3. Use the username `admin` and the randomly generated password.
4. Start transferring files between your Android device and the SFTP client!


## Contribution

Feel free to contribute by submitting a pull request or suggesting improvements in the issues section!
