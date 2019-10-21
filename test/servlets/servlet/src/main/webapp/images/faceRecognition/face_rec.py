from face_recognition import face_distance, face_encodings, load_image_file
import os
import sys
from glob import glob
from PIL import Image
from numpy import fromfile, ndarray


def image_to_data_name(image_name):
    return image_name[:-4] + '.dat'


def get_file_name(path):
    assert isinstance(path, str)
    return '.'.join(path.split('\\')[-1].split('.')[:-1])


WORKING_DIRECTORY = sys.argv[1]
THRESHOLD = 0.5
MAX_RESULT = 10

known_faces = {}
for image_name in glob(WORKING_DIRECTORY + "*.jpg"):
    data_name = image_to_data_name(image_name)
    file_name = get_file_name(image_name)

    if os.path.isfile(data_name):
        known_faces[file_name] = fromfile(data_name)
    else:
        temp_encoding = face_encodings(load_image_file(image_name))[0]
        temp_encoding.tofile(data_name)
        known_faces[file_name] = temp_encoding

while True:
    try:
        file_path = input()
    except EOFError:
        break
    print(file_path)
    image = load_image_file(file_path)
    face_encoding = face_encodings(image)[0]

    result = [(name, face_distance([face], face_encoding)[0]) for name, face in known_faces.items()]
    result.sort(key = lambda x: x[1])

    # use binary search to remove too far faces
    l, r = 0, len(result)
    while l < r:
        mid = (l + r) >> 1  # no overflow
        if result[mid][1] < THRESHOLD:
            l = mid + 1
        elif result[mid][1] == THRESHOLD:
            l, r = mid, mid
        else:
            r = mid
    assert l == r
    n = min(l, MAX_RESULT, len(result))
    print(n)
    for name, distance in result[:n]:
        print(name)
        print(distance)