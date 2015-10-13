// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.shared.youngandroid;

import com.google.appinventor.shared.properties.json.JSONObject;
import com.google.appinventor.shared.properties.json.JSONParser;

/**
 * Methods for analyzing the contents of a Young Android Form file.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class YoungAndroidSourceAnalyzer {

  // TODO(user) Source these from a common constants library.
  public static final String SRC_FOLDER = "src";

  /**
   * The filename extension for the file which contains the component
   * properties information generated by the ODE client.
   */
  public static final String FORM_PROPERTIES_EXTENSION = ".scm";

  /**
   * The filename extension for the file which contains block metadata to be
   * consumed by codeblocks.
   */
  public static final String CODEBLOCKS_SOURCE_EXTENSION = ".blk";

  /**
   * The filename extension for the file which contains block data to be
   * consumed by blockly (the in-browser blocks editor).
   */
  public static final String BLOCKLY_SOURCE_EXTENSION = ".bky";
  
  /**
   * The filename extension for the generated Yail file. Even though it is generated by the system
   * and can be reconstructed from the .scm and .bky files it is stored as a source file.
   */
  public static final String YAIL_FILE_EXTENSION = ".yail";

  public static final String JAVA_FILE_EXTENSION = ".java";

  private static final String FORM_PROPERTIES_PREFIX = "#|\n";
  private static final String FORM_PROPERTIES_SUFFIX = "\n|#";

  private YoungAndroidSourceAnalyzer() {
  }

  /**
   * Parses a complete source file and return the properties as a JSONObject.
   *
   * @param source a complete source file
   * @param jsonParser a JSON parser
   * @return the properties as a JSONObject
   */
  public static JSONObject parseSourceFile(String source, JSONParser jsonParser) {
    // First, locate the beginning of the $JSON section.
    // Older files have a $Properties before the $JSON section and we need to make sure we skip
    // that.
    String jsonSectionPrefix = FORM_PROPERTIES_PREFIX + "$JSON\n";
    int beginningOfJsonSection = source.lastIndexOf(jsonSectionPrefix);
    if (beginningOfJsonSection == -1) {
      throw new IllegalArgumentException(
          "Unable to parse file - cannot locate beginning of $JSON section");
    }
    beginningOfJsonSection += jsonSectionPrefix.length();

    // Then, locate the end of the $JSON section;
    String jsonSectionSuffix = FORM_PROPERTIES_SUFFIX;
    int endOfJsonSection = source.lastIndexOf(jsonSectionSuffix);
    if (endOfJsonSection == -1) {
      throw new IllegalArgumentException(
          "Unable to parse file - cannot locate end of $JSON section");
    }

    String jsonPropertiesString = source.substring(beginningOfJsonSection,
        endOfJsonSection);
    return jsonParser.parse(jsonPropertiesString).asObject();
  }

  /**
   * Generates a complete source file from the given properties
   *
   * @param propertiesObject the properties as a JSONObject
   * @return the complete source file
   */
  public static String generateSourceFile(JSONObject propertiesObject) {
    String jsonPropertiesSection = "$JSON\n" + propertiesObject.toJson();
    return FORM_PROPERTIES_PREFIX + jsonPropertiesSection + FORM_PROPERTIES_SUFFIX;
  }
}
