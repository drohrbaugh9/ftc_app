# # CHANGEME: This should be the eigen3 path

EIGEN3_PATH = /usr/local/include/eigen3

# # Name of the executable
EXE = crypto

OBJS = crypto.o

# # C++ Compiler command
CXX = g++

# # C++ Compiler options
CXXFLAGS = -O3 -std=c++0x -pipe -Wall -Wextra -pedantic

# # Include directories 
INCL =  -I$(EIGEN3_PATH) -I/usr/local/include  `pkg-config --cflags opencv`

# # Linker flags
LIBS = -L/usr/local/lib -L/usr/local/lib -L/usr/local/Cellar/opencv/3.3.1_1/lib -lopencv_stitching -lopencv_superres -lopencv_videostab -lopencv_photo -lopencv_aruco -lopencv_bgsegm -lopencv_bioinspired -lopencv_ccalib -lopencv_dpm -lopencv_face -lopencv_fuzzy -lopencv_img_hash -lopencv_line_descriptor -lopencv_optflow -lopencv_reg -lopencv_rgbd -lopencv_saliency -lopencv_stereo -lopencv_structured_light -lopencv_phase_unwrapping -lopencv_surface_matching -lopencv_tracking -lopencv_datasets -lopencv_text -lopencv_dnn -lopencv_plot -lopencv_xfeatures2d -lopencv_shape -lopencv_video -lopencv_ml -lopencv_ximgproc -lopencv_calib3d -lopencv_features2d -lopencv_highgui -lopencv_videoio -lopencv_flann -lopencv_xobjdetect -lopencv_imgcodecs -lopencv_objdetect -lopencv_xphoto -lopencv_imgproc -lopencv_core

all: $(EXE)

$(EXE): $(OBJS)
	$(CXX) -o $(EXE) $(OBJS) $(LIBS)

clean:
	rm -rf $(EXE) $(OBJS)
    
.cpp.o:
	$(CXX) -c $(CXXFLAGS) $(INCL) -o $@ $<


