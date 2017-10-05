package deltatimo.gta5.privatelobby;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IPv4 implements Comparable<IPv4> {
  public int firstRange = 0;
  public int secondRange = 0;
  public int thirdRange = 0;
  public int fourthRange = 0;
  public static int max = 255;
  boolean isValid = true;

  public IPv4(int firstRange, int secondRange, int thirdRange, int fourthRange) {
    if (firstRange > max || secondRange > max || thirdRange > max || fourthRange > max
        || firstRange < 0 || secondRange < 0 || thirdRange < 0 || fourthRange < 0) {
      isValid = false;
      this.firstRange = 0;
      this.secondRange = 0;
      this.thirdRange = 0;
      this.fourthRange = 0;
    } else {
      this.firstRange = firstRange;
      this.secondRange = secondRange;
      this.thirdRange = thirdRange;
      this.fourthRange = fourthRange;
    }
  }

  private IPv4() {}

  private IPv4(boolean valid) {
    isValid = valid;
  }

  @Override
  public String toString() {
    return this.firstRange + "." + this.secondRange + "." + this.thirdRange + "."
        + this.fourthRange;
  }

  public boolean isValid() {
    return isValid;
  }

  public static IPv4 getFromString(String ipv4) {
    Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");
    Matcher matcher = pattern.matcher(ipv4);
    matcher.find();
    try {
      return new IPv4(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
          Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
    } catch (NumberFormatException | IllegalStateException e) {
      return new IPv4(false);
    }
  }

  public IPv4 getPreceding() {
    if (this.fourthRange == 0) {
      if (this.thirdRange == 0) {
        if (this.secondRange == 0) {
          if (this.firstRange == 0) {
            return new IPv4(false);
          } else
            return new IPv4(this.firstRange - 1, max, max, max);
        } else
          return new IPv4(this.firstRange, this.secondRange - 1, max, max);
      } else
        return new IPv4(this.firstRange, this.secondRange, this.thirdRange - 1, max);
    } else
      return new IPv4(this.firstRange, this.secondRange, this.thirdRange, this.fourthRange - 1);
  }

  public IPv4 getFollowing() {
    if (this.fourthRange == max)
      if (this.thirdRange == max)
        if (this.secondRange == max)
          if (this.firstRange == max)
            return new IPv4(false);
          else
            return new IPv4(this.firstRange + 1, 0, 0, 0);
        else
          return new IPv4(this.firstRange, this.secondRange + 1, 0, 0);
      else
        return new IPv4(this.firstRange, this.secondRange, this.thirdRange + 1, 0);
    else
      return new IPv4(this.firstRange, this.secondRange, this.thirdRange, this.fourthRange + 1);
  }

  private long value() {
    return (long) (Math.pow(max, 6) * (this.firstRange + 1)
        + Math.pow(max, 4) * (this.secondRange + 1) + max * (this.thirdRange + 1)
        + this.fourthRange);
  }

  @Override
  public int compareTo(IPv4 other) {
    if (this.firstRange - other.firstRange > 0)
      return 1;
    if (this.firstRange - other.firstRange < 0)
      return -1;

    if (this.secondRange - other.secondRange > 0)
      return 1;
    if (this.secondRange - other.secondRange < 0)
      return -1;

    if (this.thirdRange - other.thirdRange > 0)
      return 1;
    if (this.thirdRange - other.thirdRange < 0)
      return -1;

    if (this.fourthRange - other.fourthRange > 0)
      return 1;
    if (this.fourthRange - other.fourthRange < 0)
      return -1;
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + firstRange;
    result = prime * result + fourthRange;
    result = prime * result + secondRange;
    result = prime * result + thirdRange;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IPv4 other = (IPv4) obj;
    if (firstRange != other.firstRange)
      return false;
    if (fourthRange != other.fourthRange)
      return false;
    if (secondRange != other.secondRange)
      return false;
    if (thirdRange != other.thirdRange)
      return false;
    return true;
  }
}

