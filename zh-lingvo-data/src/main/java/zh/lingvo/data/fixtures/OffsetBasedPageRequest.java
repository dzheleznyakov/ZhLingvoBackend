package zh.lingvo.data.fixtures;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;
    private final String[] sortedColumns;

    public OffsetBasedPageRequest(int limit, int offset, String... sortedColumns) {
        Preconditions.checkArgument(limit >= 1, "Limit must be greater than or equal to 1");
        Preconditions.checkArgument(offset >= 0, "Offset must be greater than or equal to 0");
        Preconditions.checkNotNull(sortedColumns, "Column to sort by cannot be null");

        this.limit = limit;
        this.offset = offset;
        this.sort = Sort.by(Sort.Direction.ASC, sortedColumns);
        this.sortedColumns = sortedColumns;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(getPageSize(), (int)(getOffset() + getPageSize()), sortedColumns);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious()
                ? new OffsetBasedPageRequest(getPageSize(), (int)(getOffset() - getPageSize()), sortedColumns)
                : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0, sortedColumns);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
