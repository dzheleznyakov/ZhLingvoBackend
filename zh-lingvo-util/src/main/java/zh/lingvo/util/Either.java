package zh.lingvo.util;

import java.util.NoSuchElementException;

public abstract class Either<L, R> {
    public abstract boolean isLeft();

    public boolean isRight() {
        return !isLeft();
    }

    public abstract L getLeft();

    public abstract R getRight();

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    private static class Left<L, R> extends Either<L, R> {
        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("No right element in left Either");
        }
    }

    private static class Right<L, R> extends Either<L, R> {
        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("No left element in the right Either");
        }

        @Override
        public R getRight() {
            return value;
        }
    }
}
