import type React from 'react';

import Header from './header/Header';

type Props = {
  children: React.ReactNode | React.ReactNode[];
};

const Layout = ({ children }: Props) => {
  return (
    <div>
      <Header />
      {children}
    </div>
  );
};

export default Layout;
