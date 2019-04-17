import socket
from threading import Thread

from .components import components

class HTTPServer(object):
    def __init__(self, port=5800):
        # Create and configure TCP socket
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind(("0.0.0.0", port))
        self.sock.listen(1)

        # Create list of threads
        self.threads = []
    
    def __accept(self, conn, addr):
        data = conn.recv(1024)
        if not data: return
        
        payload = """
        <head>
            <title>5024 Webview</title>
            <style>
                table, th, td {
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 5;
                }
            </style>
        </head>
        <h1>Raider Robotics Webview</h1>
        <b>Active Components:</b>
        <table>
        <tr><th>Name</th><th>Type</th></tr>
        """

        # Append each component to the payload
        for component in components:
            payload += f"<tr><td>{component}</td><td>{components[component]['type']}</td></tr>"
        
        payload += "</table>"
        
        response = "HTTP/1.1 200 OK\n\rContent-Type: text/html; charset=UTF-8\n\r\n\r"+ payload

        conn.send(response.encode())
        conn.close()
    
    def __run(self):
        while True:
            conn, addr = self.sock.accept()
            self.threads.append(Thread(target=self.__accept, args=(conn, addr)))
            self.threads[-1].start()
    
    def Start(self):
        listener = Thread(target=self.__run)
        listener.start()

