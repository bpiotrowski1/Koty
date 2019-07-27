package pl.bpiotrowski;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class Cat {
    @SerializedName("file")
    @Getter private String catUrl;
}
