package com.csz.github.view.config;

/**
 * filter some mime type resources without caching.
 * <p>
 * @author caishuzhan
 */
public interface MimeTypeFilter {

    boolean isFilter(String mimeType);

    void addMimeType(String mimeType);

    void removeMimeType(String mimeType);

    void clear();

}
