package frc.robot.common;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;

public class Camera {
    UsbCamera camera;
    MjpegServer camera_server;

    public Camera(String name, int http_port) {
        this.camera = CameraServer.getInstance().startAutomaticCapture();
        this.camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15);
        this.camera_server = new MjpegServer(name, http_port);
        this.camera_server.setSource(this.camera);
    }

    public Camera(String name, int http_port, int usb_port) {
        this.camera = CameraServer.getInstance().startAutomaticCapture(usb_port);
        this.camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15);
        this.camera_server = new MjpegServer(name, http_port);
        this.camera_server.setSource(this.camera);
    }

    public void setResolution(int height, int width, int fps) {
        this.camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, height, width, fps);
    }
}