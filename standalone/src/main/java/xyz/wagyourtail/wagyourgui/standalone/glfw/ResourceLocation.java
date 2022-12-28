package xyz.wagyourtail.wagyourgui.standalone.glfw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public record ResourceLocation(String domain, String path) {
    public static ResourceLocation of(String domain, String path) {
        return new ResourceLocation(domain, path);
    }

    public static ResourceLocation of(String path) {
        if (path.contains(":")) {
            String[] split = path.split(":");
            return new ResourceLocation(split[0], split[1]);
        } else {
            return new ResourceLocation("wagyourgui", path);
        }
    }

    public static ResourceLocation relative(ResourceLocation loc, String path) {
        return new ResourceLocation(loc.domain, loc.path + "/" + path);
    }

    public static ResourceLocation parent(ResourceLocation loc) {
        return new ResourceLocation(loc.domain, loc.path.substring(0, loc.path.lastIndexOf('/')));
    }

    public String toString() {
        return domain + ":" + path;
    }

    public String getPath() {
        return path;
    }

    public String getDomain() {
        return domain;
    }

    public String getLocation() {
        if (domain.equals("wagyourgui")) {
            return "assets/" + path;
        } else {
            return "assets/" + domain + "/" + path;
        }
    }

    public InputStream getResource() throws IOException {
        Path path = Paths.get(this.getLocation());
        if (Files.isReadable(path)) {
            return Files.newInputStream(path);
        } else {
            InputStream stream = ResourceLocation.class.getClassLoader().getResourceAsStream(this.getLocation());
            if (stream == null) {
                throw new IOException("Resource not found: " + this.getLocation());
            }
            return stream;
        }
    }
}
