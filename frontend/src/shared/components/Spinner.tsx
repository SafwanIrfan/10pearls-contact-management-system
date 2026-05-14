interface SpinnerProps {
  size?: number;
}

export const Spinner = ({ size = 20 }: SpinnerProps) => (
  <div
    className="rounded-full border-2 border-gray-200 border-t-button animate-spin"
    style={{ width: size, height: size }}
  />
);
