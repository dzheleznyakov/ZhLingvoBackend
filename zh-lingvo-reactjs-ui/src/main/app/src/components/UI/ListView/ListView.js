import React from 'react';
import PropTypes from 'prop-types';

import classes from './ListView.module.scss';


const ListView = props => {
    const { items, onItemClick, width, height, selectedIndex } = props;
    
    const getItemClassName = index => (index === selectedIndex ? classes.SelectedWord : null);
    const style = { width, minHeight: height, height };

    return (
        <ul className={classes.ListViewPort} style={style}>
            {items.map(({ key, node }, i) => (
                <li 
                    key={key}
                    className={getItemClassName(i)}
                    onClick={onItemClick(i)}
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
    height: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
    selectedIndex: PropTypes.number,
};

ListView.defaultProps = {
    items: [],
    onItemClick: () => {},
    selectedIndex: -1,
};

export default ListView;
