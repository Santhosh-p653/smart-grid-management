package com.smartgrid.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 100)
    private String region;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GridNode> gridNodes = new ArrayList<>();

    public Zone() {}

    public Zone(Long id, String name, String description, String region, List<GridNode> gridNodes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.region = region;
        this.gridNodes = gridNodes != null ? gridNodes : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<GridNode> getGridNodes() { return gridNodes; }
    public void setGridNodes(List<GridNode> gridNodes) { this.gridNodes = gridNodes; }

    public static ZoneBuilder builder() {
        return new ZoneBuilder();
    }

    public static class ZoneBuilder {
        private Long id;
        private String name;
        private String description;
        private String region;
        private List<GridNode> gridNodes = new ArrayList<>();

        public ZoneBuilder id(Long id) { this.id = id; return this; }
        public ZoneBuilder name(String name) { this.name = name; return this; }
        public ZoneBuilder description(String description) { this.description = description; return this; }
        public ZoneBuilder region(String region) { this.region = region; return this; }
        public ZoneBuilder gridNodes(List<GridNode> gridNodes) { this.gridNodes = gridNodes; return this; }

        public Zone build() {
            return new Zone(id, name, description, region, gridNodes);
        }
    }
}
