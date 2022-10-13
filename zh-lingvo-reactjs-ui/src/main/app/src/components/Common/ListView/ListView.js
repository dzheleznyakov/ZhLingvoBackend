import React, { useState } from 'react';
import PropTypes from 'prop-types';

import classes from './ListView.module.scss';


const ListView = props => {
    const { items, onItemClick, width, defaultSlectedIndex } = props;
    
    let defSelInd;
    switch (typeof defaultSlectedIndex) {
        case 'function': defSelInd = defaultSlectedIndex(); break;
        case 'number': defSelInd = defaultSlectedIndex; break;
        default: defSelInd = -1;
    }
    const [selectedIndex, setSelectedIndex] = useState(defSelInd);
    const getItemClassName = index => (index === selectedIndex ? classes.SelectedWord : null);
    const onClickFactory = index => event => {
        setSelectedIndex(index);
        onItemClick(index)(event);
    };
    const style = { width };
    return (
        <ul className={classes.ListViewPort} style={style}>
            {items.map(({ key, node }, i) => (
                <li 
                    key={key}
                    className={getItemClassName(i)}
                    onClick={onClickFactory(i)}
                >
                    {node}
                </li>
            ))}
        </ul>
    );
};

ListView.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        key: PropTypes.string.isRequired,
        node: PropTypes.node.isRequired,
    })),
    onItemClick: PropTypes.func,
    width: PropTypes.number,
    defaultSlectedIndex: PropTypes.any,
};

ListView.defaultProps = {
    items: [],
    onItemClick: () => {},
    defaultSlectedIndex: -1,
};

export default ListView;
