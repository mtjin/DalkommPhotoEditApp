import copy
import numpy as np
import cv2

import faceRecognition

class processEdit:  #이미지 편집 처리기능

    def __init__(self):
        self.processImg = ''

    def setFilename(self, fn):
        self.processImg = fn

    def cutting(self):
        face = faceRecognition.processFace()
        self.processImg = face.recognition()  #오리기 처리 후 결과값 클래스 변수에 할당(배열)
        return self.processImg

    def filter(self, img, code):  #code에 맞는 필터를 찾아 기능수행
        if code == '': #엠보싱 효과
            kernel = np.mat([[-1, 0, 0], [0, 0, 0], [0, 0, 1]])
            dst = cv2.filter2D(img, cv2.CV_8U, kernel) #커널을 이용한 회선 계산, 2번째 인자로 화소 깊이 적용

            self.processImg = dst
            return self.processImg

        elif code == '': #수채화 효과
            dsize = 7
            sigma = 32
            iterate = 20
            dst = ""
            i = 0
            t1 = copy.deepcopy(img)

            while i < iterate:
                if i % 2 == 0:
                    t2 = cv2.bilateralFilter(t1, dsize, sigma, sigma) #양방향 필터 : 스무딩 이미지 처리와 노이즈 제거
                else:
                    t1 = cv2.bilateralFilter(t2, dsize, sigma, sigma)
                i = i + 1

            self.processImg = t1
            return self.processImg

        elif code == '': #스캐치 효과

            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  #사진을 gray색으로 변경
            cv2.blur(gray, (3, 3), gray)  #번짐효과
            edge = cv2.Canny(gray, 5, 5 * 2, 5, 3)   #테두리 검출

            self.processImg = edge
            return self.processImg

