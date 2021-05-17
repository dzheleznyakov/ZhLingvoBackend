import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './Excerpt.module.scss';

const Excerpt = props => {
    const { children } = props;
    const wrapperRef = useRef();
    const contentRef = useRef();
    const [overflown, setOverflown] = useState(false);
    const classNames = [classes.Excerpt];
    if (overflown)
        classNames.push(classes.Dissolving);

    useEffect(() => {
        if (wrapperRef.current && contentRef.current) {
            const { height: wrapperHeight } = wrapperRef.current.getBoundingClientRect();
            const { height: contentHeight } = contentRef.current.getBoundingClientRect();
            (contentHeight > wrapperHeight) && setOverflown(true);
        }
    }, [wrapperRef, contentRef]);

    return (
        <blockquote className={classNames.join(' ')} ref={wrapperRef}>
            <div ref={contentRef}>
                {children}
            </div>
        </blockquote>
    );
};

Excerpt.propTypes = {};

Excerpt.defaultProps = {};

export default Excerpt;
