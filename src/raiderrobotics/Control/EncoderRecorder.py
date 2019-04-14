import pickle

class Recorder(object):
    def __init__(self, file_path: str, left_callback, right_callback, gyro_callback):
        self.left = left_callback
        self.right = right_callback
        self.gyro = gyro_callback
        self.file_path = file_path

        self.recording = []
    
    def Feed(self, speed, rotation):
        datapoint = (speed, rotation, self.gyro())
        print(datapoint)
        self.recording.append(datapoint)
    
    def Save(self):
        with open(self.file_path, "wb") as f:
            pickle.dump(self.recording, f)

class Player(object):
    def __init__(self, file_path: str):
        with open(file_path, "rb") as f:
            self.recording = pickle.load(f)
            f.close()
        self.index = 0
    
    def Play(self):
        self.index += 1
        if self.index <= len(self.recording):
            return self.recording[self.index - 1]
        else:
            return (0, 0, 0)
    
    def Reset(self):
        self.index = 0