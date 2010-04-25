#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <vector>

using namespace std;

struct ImageClassification {
  bool nutritious;
  int line;
  int image[6][6];

  ImageClassification() {
    nutritious = false;
    line = 0;
  }
};

int main(int argc, char ** argv) {
  char line[1024];
  int a, b, c, d, e, f;

  vector<ImageClassification> classifications;

  ImageClassification current_image;
  while(fgets(line, sizeof(line), stdin) != NULL) {
    if (strncmp(line, "nutritious", strlen("nutritious")) == 0) {
      current_image.nutritious = true;
      classifications.push_back(current_image);
      current_image = ImageClassification();
    } else if (strncmp(line, "poisonous", strlen("poisonous")) == 0) {
      current_image.nutritious = false;
      classifications.push_back(current_image);
      current_image = ImageClassification();
    } else if (sscanf(line, "%d %d %d %d %d %d", &a, &b, &c, &d, &e, &f) == 6) {
      current_image.image[current_image.line][0] = a;
      current_image.image[current_image.line][1] = b;
      current_image.image[current_image.line][2] = c;
      current_image.image[current_image.line][3] = d;
      current_image.image[current_image.line][4] = e;
      current_image.image[current_image.line][5] = f;
      current_image.line++;
    } else {
      fprintf(stderr, "Bad line: '%s'\n", line);
      break;
    }
  }

  printf("Got %d images\n", classifications.size());
}
 
