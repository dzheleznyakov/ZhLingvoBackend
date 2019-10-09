package zh.lingvo.domain.changepatterns;

import com.google.common.collect.ImmutableList;

public interface ChangeModel {
    ChangeModel EMPTY = new ChangeModel() {
        private ImmutableList<Object> emptyDimension = ImmutableList.of();

        public ImmutableList<Object> getEmptyDimension() {
            return emptyDimension;
        }

        public void setEmptyDimension(ImmutableList<Object> emptyDimension) {
            this.emptyDimension = emptyDimension;
        }
    };
}
