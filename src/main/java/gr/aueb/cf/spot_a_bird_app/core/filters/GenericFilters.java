package gr.aueb.cf.spot_a_bird_app.core.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public abstract class GenericFilters {
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String DEFAULT_SORT_COLUMN = "id";
    private final static Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    private int page;
    private int pageSize;
    private Sort.Direction sortDirection;
    private String sortBy;

    public int getPage() {
        return Math.max(page, 0);
    }

    public int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection != null ? sortDirection : DEFAULT_SORT_DIRECTION;
    }

    public String getSortBy() {
        return (sortBy == null || sortBy.isBlank()) ? DEFAULT_SORT_COLUMN : sortBy;
    }

    public Sort getSort() {
        return Sort.by(getSortDirection(), getSortBy());
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage(), getPageSize(), getSort());
    }
}