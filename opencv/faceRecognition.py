import cv2

class processFace:  #얼굴 인식 처리기능
    def __init__(self):
        self.processImg = ""

    def recognition(self, img):  #인식기능
        eye_detect = False
        face = cv2.CascadeClassifier("./haarcascade_frontalface_default.xml")
        eye = cv2.CascadeClassifier("./haarcascade_eye.xml")

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = face.detectMultiScale(gray, 1.3, 5)

        for(x,y,w,h) in faces:
            cv2.rectangle(img, (x,y), (x+w, y+h), (255,0,0), 2)
            if eye_detect:
                roi_gray = gray[y:y+h, x:x+w]
                roi_color = img[y:y+h, x:x+w]
                eyes = eye.detectMultiScale(roi_gray)
                for(ex, ey, ew, eh) in eyes:
                    cv2.rectangle(roi_color, (ex,ey), (ex+ew, ey+eh), (0,255,0), 2)

        self.processImg = img
        return self.processImg
