package com.elasticsearch.search.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SearchRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-31T00:05:06.549673800-03:00[America/Sao_Paulo]")

public class SearchRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("q")
  private String q;

  @JsonProperty("p")
  private Integer p;

  /**
   * max content length
   */
  public enum ClEnum {
    NUMBER_150(150),
    
    NUMBER_300(300),
    
    NUMBER_500(500);

    private Integer value;

    ClEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ClEnum fromValue(Integer value) {
      for (ClEnum b : ClEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("cl")
  private ClEnum cl;

  /**
   * number of items per page
   */
  public enum ItemsPerPageEnum {
    NUMBER_10(10),
    
    NUMBER_20(20),
    
    NUMBER_30(30);

    private Integer value;

    ItemsPerPageEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ItemsPerPageEnum fromValue(Integer value) {
      for (ItemsPerPageEnum b : ItemsPerPageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("itemsPerPage")
  private ItemsPerPageEnum itemsPerPage;

  public SearchRequest q(String q) {
    this.q = q;
    return this;
  }

  /**
   * query to be submitted
   * @return q
  */
  @ApiModelProperty(value = "query to be submitted")


  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public SearchRequest p(Integer p) {
    this.p = p;
    return this;
  }

  /**
   * page number
   * @return p
  */
  @ApiModelProperty(value = "page number")


  public Integer getP() {
    return p;
  }

  public void setP(Integer p) {
    this.p = p;
  }

  public SearchRequest cl(ClEnum cl) {
    this.cl = cl;
    return this;
  }

  /**
   * max content length
   * @return cl
  */
  @ApiModelProperty(value = "max content length")


  public ClEnum getCl() {
    return cl;
  }

  public void setCl(ClEnum cl) {
    this.cl = cl;
  }

  public SearchRequest itemsPerPage(ItemsPerPageEnum itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
    return this;
  }

  /**
   * number of items per page
   * @return itemsPerPage
  */
  @ApiModelProperty(value = "number of items per page")


  public ItemsPerPageEnum getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(ItemsPerPageEnum itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchRequest searchRequest = (SearchRequest) o;
    return Objects.equals(this.q, searchRequest.q) &&
        Objects.equals(this.p, searchRequest.p) &&
        Objects.equals(this.cl, searchRequest.cl) &&
        Objects.equals(this.itemsPerPage, searchRequest.itemsPerPage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, p, cl, itemsPerPage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchRequest {\n");
    
    sb.append("    q: ").append(toIndentedString(q)).append("\n");
    sb.append("    p: ").append(toIndentedString(p)).append("\n");
    sb.append("    cl: ").append(toIndentedString(cl)).append("\n");
    sb.append("    itemsPerPage: ").append(toIndentedString(itemsPerPage)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

