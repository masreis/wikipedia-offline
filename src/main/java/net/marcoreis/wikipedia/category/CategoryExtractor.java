package net.marcoreis.wikipedia.category;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoryExtractor {
    private Pattern pattern = Pattern.compile("\\[\\[Categoria\\:(.*?)\\]\\]");
    private Set<String> categories = new HashSet<String>();

    public void extractAndAddText(String texto) {

        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String categoria = matcher.group(1);

            if (categoria.contains("|")) {
                String subCategorias[] = categoria.split("\\|");
                for (String subCategoria : subCategorias) {
                    String trimmedSubCategory = subCategoria.trim();
                    if (trimmedSubCategory.length() > 0) {
                        categories.add(trimmedSubCategory);
                    }
                }
            } else {
                categories.add(categoria.trim());
            }
        }

    }

    public Set<String> getCategories() {
        return categories;
    }

}
