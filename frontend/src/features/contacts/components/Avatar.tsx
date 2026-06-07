const AVATAR_COLORS = [
  "bg-teal-100 text-teal-700",
  "bg-blue-100 text-blue-700",
  "bg-violet-100 text-violet-700",
  "bg-amber-100 text-amber-700",
  "bg-rose-100 text-rose-700",
];
const SIZES = {
  sm: "w-8 h-8 text-xs",
  md: "w-10 h-10 text-sm",
  lg: "w-16 h-16 text-xl",
};

const Avatar = ({
  name,
  size = "md",
}: {
  name: string;
  size?: keyof typeof SIZES;
}) => {
  const initials = name
    .split(" ")
    .map((n) => n[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();
  const color =
    AVATAR_COLORS[(name.codePointAt(0) ?? 0) % AVATAR_COLORS.length];

  return (
    <div
      className={`rounded-full flex items-center justify-center font-semibold shrink-0 ${SIZES[size]} ${color}`}
    >
      {initials}
    </div>
  );
};

export default Avatar;
