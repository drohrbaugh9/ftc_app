import numpy as np

import matplotlib.pyplot as plt
import sys
from subprocess import call

from moviepy.editor import VideoFileClip

framectr = 0

def annotate_image(image,lightthreshold):

    global framectr
    ymax = 265

    xsize = image.shape[0]
    ysize = image.shape[1]

    fname = "output/smooth%06d.dat" % framectr

    try:
        with open(fname,'rb') as fid:
            numsmooths = np.fromfile(fid,dtype=np.int32,count=1)[0]
            imx = np.fromfile(fid,dtype=np.int32,count=1)[0]
            arr = np.zeros((imx,numsmooths+1))

            for ii in range(numsmooths+1):
                arr[:,ii] = np.fromfile(fid,dtype=np.float32,count=imx)
            
            numcols = np.fromfile(fid,dtype=np.int32,count=1)[0]
            cols = np.fromfile(fid,dtype=np.int32,count=numcols)

            fig, ax = plt.subplots()
            ax.imshow(image, extent=[0, xsize, 0, ysize])
            ax.plot(arr[:,0]*ysize/(ymax), 'b', linewidth=4)
            ax.plot(arr[:,-1]*ysize/(ymax), 'g', linewidth=4)
            ax.plot(cols,arr[cols,-1]*ysize/(ymax),'+r',markersize=10)
            plt.axis([0,xsize,0,ysize])
            figname = 'figs/key{0:06d}.png'.format(framectr)
            plt.savefig(figname)
            plt.close()

    except IOError:
        return image

    framectr+=1

    return image


lightthreshold = 100.0
np.set_printoptions(linewidth=500)

inputmovie = 'VID_20180108_235359658.mp4'
video_output = 'processedVid.mp4'

clip1 = VideoFileClip(inputmovie).subclip(0,1)
crypto_clip = clip1.fl_image(lambda image: annotate_image(image,lightthreshold)) 
get_ipython().run_line_magic('time', 'crypto_clip.write_videofile(video_output, audio=False)')

from subprocess import call
print("making movie ...")
call(["convert", "-quality","100","figs/*.png","figs/key.mpeg"])
print("movie complete")
    
print("DONE!!!")
