package com.elasticsearch.search.api.model;

import java.util.Objects;
import com.elasticsearch.search.api.model.Result;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Results
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-27T11:02:38.472659500-03:00[America/Sao_Paulo]")

public class Results  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("totalResults")
  private Long totalResults;

  @JsonProperty("page")
  private Long page;

  @JsonProperty("itemsPerPage")
  private Long itemsPerPage;

  @JsonProperty("resultsList")
  @Valid
  private List<Result> resultsList = null;

  @JsonProperty("suggestions")
  @Valid
  private List<String> suggestions = null;

  public Results totalResults(Long totalResults) {
    this.totalResults = totalResults;
    return this;
  }

  /**
   * Get totalResults
   * @return totalResults
  */
  @ApiModelProperty(value = "")


  public Long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Long totalResults) {
    this.totalResults = totalResults;
  }

  public Results page(Long page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
  */
  @ApiModelProperty(value = "")


  public Long getPage() {
    return page;
  }

  public void setPage(Long page) {
    this.page = page;
  }

  public Results itemsPerPage(Long itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
    return this;
  }

  /**
   * Get itemsPerPage
   * @return itemsPerPage
  */
  @ApiModelProperty(value = "")


  public Long getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(Long itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  public Results resultsList(List<Result> resultsList) {
    this.resultsList = resultsList;
    return this;
  }

  public Results addResultsListItem(Result resultsListItem) {
    if (this.resultsList == null) {
      this.resultsList = new ArrayList<Result>();
    }
    this.resultsList.add(resultsListItem);
    return this;
  }

  /**
   * Get resultsList
   * @return resultsList
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Result> getResultsList() {
    return resultsList;
  }

  public void setResultsList(List<Result> resultsList) {
    this.resultsList = resultsList;
  }

  public Results suggestions(List<String> suggestions) {
    this.suggestions = suggestions;
    return this;
  }

  public Results addSuggestionsItem(String suggestionsItem) {
    if (this.suggestions == null) {
      this.suggestions = new ArrayList<String>();
    }
    this.suggestions.add(suggestionsItem);
    return this;
  }

  /**
   * Search suggestions when there are no results
   * @return suggestions
  */
  @ApiModelProperty(value = "Search suggestions when there are no results")


  public List<String> getSuggestions() {
    return suggestions;
  }

  public void setSuggestions(List<String> suggestions) {
    this.suggestions = suggestions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Results results = (Results) o;
    return Objects.equals(this.totalResults, results.totalResults) &&
        Objects.equals(this.page, results.page) &&
        Objects.equals(this.itemsPerPage, results.itemsPerPage) &&
        Objects.equals(this.resultsList, results.resultsList) &&
        Objects.equals(this.suggestions, results.suggestions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalResults, page, itemsPerPage, resultsList, suggestions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Results {\n");
    
    sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    itemsPerPage: ").append(toIndentedString(itemsPerPage)).append("\n");
    sb.append("    resultsList: ").append(toIndentedString(resultsList)).append("\n");
    sb.append("    suggestions: ").append(toIndentedString(suggestions)).append("\n");
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

