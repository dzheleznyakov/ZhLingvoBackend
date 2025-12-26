import Header from './header/Header';

type Props = {
  children: React.ReactNode | React.ReactNode[];
};

const Layout = ({ children }: Props) => {
  return (
    <div>
      <Header />
      <div className="page">{children}</div>
    </div>
  );
};

export default Layout;
